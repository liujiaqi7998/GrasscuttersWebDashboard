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

import quest from './json/quest_data.json'

const {text, isSupported, copy} = useClipboard()

var value2 = ref()
var num = ref()


const options = reactive(quest)
const message = Message

function add_quest() {
  send(`quest add ${value2.value}`);
}

function finish_quest() {
  send(`quest finish ${value2.value}`);
}

function Del_quest() {
  const send_msg = {
    type: 'DelQuest',
    data: value2.value,
  }
  const send_msg_str = JSON.stringify(send_msg)
  appStore.socketSend(send_msg_str)
}

function DelAll() {
  const send_msg = {
    type: 'DelQuest',
    data: 'ALL',
  }
  const send_msg_str = JSON.stringify(send_msg)
  appStore.socketSend(send_msg_str)
}
</script>

<template>
  <div class="commuse">
    <div class="title"> 剧情管理 </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 剧情名称:</div>
      <a-cascader allow-search v-model="value2" :options="options" placeholder="" filterable/>
    </div>

    <a-space direction="vertical">
      <a-button type="outline" @click="add_quest">添加剧情</a-button>
      <a-button type="outline" @click="finish_quest">完成剧情</a-button>
      <a-button type="outline" @click="Del_quest">删除该剧情</a-button>
      <a-button type="outline" @click="DelAll">玩坏了？清空剧情</a-button>
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
