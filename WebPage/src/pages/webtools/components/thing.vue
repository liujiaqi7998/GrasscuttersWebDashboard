
<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { useClipboard } from '@vueuse/core'
import thing from './json/thing.json'
import { Message } from '@arco-design/web-vue'
const { text, isSupported, copy } = useClipboard()
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


var value2 = ref()
var value3 = ref('give')
var num = ref()

const value = computed(() => {
  return `${value3.value} ${value2.value} ${num.value}`
})
const options = reactive(thing)
const options2 = reactive([
  {
    label: '给予指定玩家一定数量的物品',
    value: 'give',
  },
  {
    label: '在指定玩家周围掉落指定物品',
    value: 'drop',
  },
])
const message = Message

function copyvalue() {
  send(value.value);
}
</script>

<template>
  <div class="commuse">

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 获取方式: </div>
      <a-cascader allow-search v-model="value3" :options="options2" placeholder="" filterable />
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 物品: </div>
      <a-select
        allow-search
        v-model="value2"
        :options="options"
        placeholder="请输入物品"
        filterable
      />
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 数量: </div>
      <a-input-number
        v-model="num"
        placeholder="请输入数量"
        mode="button"
        size="large"
        class="input-demo"
      />
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
