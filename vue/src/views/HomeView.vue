<template>
  <!-- 顶部区域 -->
  <div style="margin: 10px 10px; display: flex; justify-content: space-between;">
    <div style="display: flex;">
      <el-input v-model="name" style="width: 50%;" placeholder="输入姓名"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="handleSearch">搜索</el-button>
      <el-button type="success" style="margin-left: 5px" @click="handleCollect">收藏的联系人</el-button>
    </div>
    <div style="display: flex;">
      <el-button type="primary" style="margin-left: 5px" @click="handleExport">导出</el-button>
      <el-upload
          :action="$baseUrl + '/user/import'"
          accept=".xlsx"
          :auto-upload="true"
          :show-file-list="false"
          style="margin-left: 5px;"
          :headers="headers"
          :data="uploadParams"
          @success="handleImportSuccess"
          @error="handleImportError"
      >
        <el-button type="primary">导入</el-button>
      </el-upload>
      <el-button type="primary" style="margin-left: 5px;" @click="showAddDialog">新增</el-button>
<!--      <el-button plain style="margin-left: auto;" @click="showAddDialog">+</el-button>-->
    </div>
  </div>

  <!-- 表格区域 -->
  <div style="padding: 10px;">
    <el-table :data="data.arr" border :row-class-name="rowClassName">
    <el-table-column type="selection" />
    <el-table-column prop="id" label="编号" width="55" />
    <el-table-column prop="name" label="姓名" />
    <el-table-column prop="phone" label="手机号码" />
    <el-table-column prop="email" label="邮箱" />
    <el-table-column prop="socialid" label="社媒账号" />

      <el-table-column label="" width="150">
        <template #default="scope">
          <el-button @click="showEditDialog(scope.row)" size="small" type="primary">编辑</el-button>
          <!-- 删除按钮和气泡确认框 -->
          <el-popconfirm
              title="确定删除吗？"
              @confirm="confirmDelete(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="example-pagination-block">
    <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next, sizes"
        :page-sizes="[10, 100]"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>

  <!-- 新增/编辑用户弹窗 -->
  <el-dialog :title="isEditing ? '编辑' : '新增'" v-model="dialogFormVisible">
    <el-form :model="formData" label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="formData.name"></el-input>
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="formData.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="formData.email"></el-input>
      </el-form-item>
      <el-form-item label="社媒账号">
        <el-input v-model="formData.socialid"></el-input>
      </el-form-item>
      <!-- 是否收藏 -->
      <el-form-item label="是否收藏">
        <el-radio-group v-model="formData.collect">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button type="primary" @click="isEditing ? handleUpdate() : handleAdd()">确定</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import request from "@/utils/request";
import { ElMessage } from 'element-plus';
const name = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const data = ref({ arr: [] });
const visible = ref(false);
const dialogFormVisible = ref(false);
const isEditing = ref(false);
import { getCurrentInstance } from 'vue';

const { proxy } = getCurrentInstance();
const baseUrl = proxy.$baseUrl;
// 表单数据
const formData = ref({
  id: null,
  username: '',
  password: '',
  name: '',
  phone: '',
  email: '',
  socialid:'',
  collect:''
});

const rowClassName = ({ row }) => {
  return row.collect === true || row.collect === 'true' ? 'collect-row' : '';
};


const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  fetchData(); // 更新数据
};

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage;
  fetchData(); // 更新数据
};

// 获取分页数据
const fetchData = async (collect = '') => {
  try {


    const response = await request.get('/user/selectByPage', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        username: '',
        name: name.value.trim(),
        collect: collect
      }
    });
    data.value.arr = response.data.list;
    total.value = response.data.total;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

// 搜索功能
const handleSearch = () => {
  currentPage.value = 1;
  pageSize.value = 10;
  fetchData ('');
};

// 显示新增弹窗
const showAddDialog = () => {
  isEditing.value = false;
  formData.value = { id: null, username: '', password: '', name: '', phone: '', email: '', socialid: '', collect: ''  };
  dialogFormVisible.value = true;
};

// 显示编辑弹窗
const showEditDialog = (row) => {
  isEditing.value = true;
  formData.value = { ...row };
  dialogFormVisible.value = true;
};

// 新增用户
const handleAdd = async () => {
  try {
    await request.post('/user/add', formData.value);
    dialogFormVisible.value = false;
    fetchData();
  } catch (error) {
    console.error('Error adding user:', error);
  }
};

// 更新用户
const handleUpdate = async () => {
  try {
    await request.put('/user/update', formData.value);
    dialogFormVisible.value = false;
    fetchData();
  } catch (error) {
    console.error('Error updating user:', error);
  }
};

// 删除用户
const confirmDelete = async (id) => {
  try {
    await request.delete(`/user/delete/${id}`);
    fetchData();
  } catch (error) {
    console.error('Error deleting user:', error);
  }
};
//显示收藏联系人
const handleCollect = () => {
  currentPage.value = 1; // 重置为第一页
  pageSize.value = 100;
  fetchData('true'); // 调用分页接口，传入 collect = true
};

// 导出数据为 Excel
const handleExport = async () => {
  window.open(baseUrl + '/user/export')
};


// 导入成功处理
const handleImportSuccess = () => {
  ElMessage.success('数据导入成功');
  fetchData(); // 重新加载数据
};

// 导入失败处理
const handleImportError = () => {
  ElMessage.error('数据导入失败，请检查文件格式');
};


onMounted(fetchData);


</script>




<style>
.collect-row {
  background-color: #f5fff0 !important; /* 强制使用浅绿色 */
}

</style>