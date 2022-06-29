<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import {
  IconMenuFold,
  IconMenuUnfold,
  IconApps,
  IconBug,
  IconBulb,
} from '@arco-design/web-vue/es/icon';
import router from "@/router/index"
import { useAppStore } from '@/store'
import { throwStatement } from '@babel/types';
const appStore = useAppStore()
const datav = reactive([
  { name: '快速指令', path: "/webtools/other" },
  { name: '常用物品', path: "/webtools/commuse" },
  { name: '物品', path: "/webtools/thing" },
  { name: '圣遗物', path: "/webtools/holyrelic" },
  { name: '怪物生成', path: "/webtools/monster" },
  { name: '武器', path: "/webtools/weapon" },
  { name: '角色属性', path: "/webtools/role" },
  { name: '技能等级', path: "/webtools/talent" },
  { name: '场景', path: "/webtools/scene" },
  { name: '剧情', path: "/webtools/quest" }
])

const datav2 = reactive([
  { name: '传送管理', path: "/webtools/LocationManager" },
  { name: '在线玩家', path: "/webtools/personnel" },
  { name: '手动连接', path: "/webtools/login" }
])



function topath(path: string) {
  router.push({ path: path })
}

const selectedKey = ref([""])
onMounted(() => {
  selectedKey.value = [router.currentRoute.value.fullPath]
})

watch(
  () => appStore.isLogin,
  () => {
    const isLogin: boolean = appStore.isLogin
    if (isLogin) {

    } else {

    }
  },
  {
    immediate: true,
  },
)

watch(
  () => router.currentRoute.value.path,

  (newValue, oldValue) => {
    selectedKey.value = [newValue]
  },
  { immediate: true }
)
</script>
<template>
  <div class="nav ">
    <a-menu showCollapseButton :default-open-keys="['0', '1']" :selected-keys="selectedKey">
      <a-sub-menu key="0">
        <template #icon>
          <IconApps></IconApps>
        </template>
        <template #title>基础功能</template>
        <a-menu-item v-for="(item, index) in datav" :key="item.path" @click="topath(item.path)">
          {{ item.name }}
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="1">
        <template #icon>
          <IconApps></IconApps>
        </template>
        <template #title>更多功能</template>
        <a-menu-item v-for="(item, index) in datav2" :key="item.path" @click="topath(item.path)">
          {{ item.name }}
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </div>
</template>
<style lang="less" scoped>
.nav {
  height: calc(100vh - 57px);

  >div {
    height: 100%;
  }
}
</style>
