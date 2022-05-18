
<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { useClipboard } from '@vueuse/core'
import { Message } from '@arco-design/web-vue'
import { useAppStore } from '@/store/modules/app'
const appStore = useAppStore()
function send(cmd: string) {
  const send_msg = {
    type: 'CMD',
    data: cmd,
  }
  const send_msg_str = JSON.stringify(send_msg)
  appStore.socketSend(send_msg_str)
}

import role from './json/role.json'

const { text, isSupported, copy } = useClipboard()

var value2 = ref("hp")
var num = ref()


const value = computed(() => {
  return `setstats ${value2.value} ${num.value}`
})
const options = reactive(role)
const message = Message

function copyvalue() {
  send(value.value);
}
</script>

<template>
  <div class="commuse">
    <div class="title"> 直接修改当前角色的面板 </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 属性: </div>
      <a-cascader allow-search v-model="value2" :options="options" placeholder="" filterable />
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 数值: </div>
      <a-input-number v-model="num" placeholder="" mode="button" size="large" class="input-demo" />
    </div>
    <div class="generate">
      <a-input v-model="value" placeholder="" />
      <a-button type="outline" @click="copyvalue">执行</a-button>
    </div>
  </div>
</template>
<style lang="less" scoped>
.commuse {
  width: 500px;
  margin: auto;
}

.title {
  text-align: center;
  font-size: 16px;
  margin: 10px 0;
}

.commuse-item {
  display: flex;
  align-items: center;
  color: #000;
  margin: 18px 0;

  > div {
    &:nth-child(1) {
      width: 150px;
      text-align: right;
      padding-right: 10px;
    }
  }
}

.generate {
  display: flex;
  align-items: center;
  margin-left: 100px;
}
</style>
