<template>
  <div id="app">
    <MyHeader v-if="!$route.meta.hideHeader" />
    <router-view/>
  </div>
</template>

<style>
/* 这里可以添加你的样式 */
</style>

<script>
import MyHeader from "@/components/header";

export default {
  name: "Layout",
  components: {
    MyHeader
  }
}
</script>
