<script setup lang="ts">
import {reactive, ref, computed} from 'vue'
import {useClipboard} from '@vueuse/core'
import {Message} from '@arco-design/web-vue'
import {useAppStore} from '@/store/modules/app'

const appStore = useAppStore()

function send(cmd: string) {
  const send_msg = {
    type: 'CMD',
    data: cmd,
  }
  const send_msg_str = JSON.stringify(send_msg)
  appStore.socketSend(send_msg_str)
}

import scene from './json/scene_data.json'

const {text, isSupported, copy} = useClipboard()

var value2 = ref(3)
var num = ref()


const options = reactive(scene)
const message = Message

function go_in_safe() {
  send(`tp 0 10 0 ${value2.value}`);
}


function back_home() {
  send(`tp 2270.885 220.09885 -866.60046 3`);
}
</script>

<template>
  <div class="commuse">
    <div class="title"> 进入到指定场景</div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 场景名称:</div>
      <a-cascader allow-search v-model="value2" :options="options" placeholder="" filterable/>
    </div>

    <a-space direction="vertical">
      <a-button type="outline" @click="go_in_safe">安全进入（进入后传送到坐标 0 20 0）</a-button>
      <a-button type="outline" @click="back_home">玩坏了？返回蒙德城</a-button>
    </a-space>
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
