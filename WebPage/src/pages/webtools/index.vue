<script setup lang="ts">
import { reactive, ref } from 'vue'
import Header from '@/components/Header/index.vue'
import startnav from './components/startnav.vue'
import commuse from './components/commuse.vue'
import handler from "uri-js/dist/esnext/schemes/https";
import { useAppStore } from '@/store'
import { Message } from '@arco-design/web-vue'


</script>
<template>
  <Header />
  <div class="cont">
    <startnav />
    <div class="selectcom">
      <router-view></router-view>
    </div>
  </div>

</template>
<style lang="less" scoped>
@import url('./style.less');
</style>