
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

const { text, isSupported, copy } = useClipboard()

const options = reactive([
  {
    title: '治疗队伍中所有角色',
    value: 'heal',
  },
  {
    title: '列出在线玩家',
    value: 'list',
  },
  {
    title: '获取当前坐标',
    value: 'position',
  },
  {
    title: '设置世界等级为8级',
    value: 'setworldlevel 8',
  },
])
const message = Message

function copyvalue(value: string) {
  send(value);
}
</script>

<template>
  <div class="commuse">
    <div v-for="(item, index) in options" :key="index">
      <div class="text-slate-900 dark:text-slate-100">{{ item.title }}</div>
      <div>
        <a-input v-model="item.value" placeholder="" disabled />
      </div>
      <div>
        <a-button type="outline" @click="copyvalue(item.value)">执行</a-button>
      </div>
    </div>
  </div>
</template>
<style lang="less" scoped>
.commuse {
  width: 500px;
  margin: auto;

  > div {
    margin: 10px 0;
    display: flex;
    align-items: center;
    color: #000;

    > div {
      &:nth-child(1) {
        width: 130px;
      }

      margin: 0 5px;
    }
  }
}

.generate {
  display: flex;
  align-items: center;
  margin-left: 100px;
}
</style>
