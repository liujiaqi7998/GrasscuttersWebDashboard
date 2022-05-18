<template>
  <router-view></router-view>
</template>
<script setup lang="ts">
import {title} from 'process'
import {useAppStore} from './store/modules/app'

import {ref, watch, onMounted} from 'vue'
import {darkTheme} from 'naive-ui'
import {Message} from "@arco-design/web-vue";

const appStore = useAppStore()
const apptheme = ref()


watch(
    () => appStore.theme,
    () => {
      const theme: string = appStore.theme
      if (theme == 'dark') {
        apptheme.value = darkTheme
      } else {
        apptheme.value = null
      }
    },
    {
      immediate: true,
    },
)

onMounted(() => {
  function getQueryVariable(variable: string) {
    const query = window.location.href.split("?");
    if (query.length <= 1) {
      return (false);
    }
    const vars = query[1].split("&");
    for (let i = 0; i < vars.length; i++) {
      const pair = vars[i].split("=");
      if (pair[0] == variable) {
        return pair[1];
      }
    }
    return (false);
  }

  var wss = ref('')
  var ServerAddress = getQueryVariable("server");

  if (ServerAddress !== false) {
    //URL解码
    ServerAddress = decodeURIComponent(ServerAddress);
    wss = ref(`${ServerAddress}?key=${getQueryVariable("key")}`)
    console.log(wss.value)
    if (wss.value.indexOf("ws://") < 0 && wss.value.indexOf("wss://") < 0) {
      Message.error(`地址不正确`)
    } else {
      appStore.socketConnect(wss.value)
    }
  }
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: var(--color-bg-1);
  overflow: hidden;
  min-height: 100vh;
}
</style>
