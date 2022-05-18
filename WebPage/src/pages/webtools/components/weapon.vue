
<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { useClipboard } from '@vueuse/core'
import { Message } from '@arco-design/web-vue'
import weapon from './json/weapon.json'
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

var value2 = ref(12510)
var num = ref(5)
var grade = ref(90)
var refined = ref(5)

const value = computed(() => {
  return `give ${value2.value} ${num.value} ${grade.value} ${refined.value}`
})
const options = reactive(weapon)

function copyvalue() {
  send(value.value);
}
</script>

<template>
  <div class="commuse">

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 武器: </div>
      <a-cascader
        allow-search
        v-model="value2"
        :options="options"
        placeholder="请输入物品"
        filterable
      />
    </div>
    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 数量: </div>
      <a-input-number v-model="num" placeholder="" mode="button" size="large" class="input-demo" />
    </div>
    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 等级: </div>
      <a-input-number
        v-model="grade"
        placeholder="请输入"
        mode="button"
        size="large"
        class="input-demo"
      />
    </div>
    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 精炼等级: </div>
      <a-input-number
        v-model="refined"
        placeholder="请输入"
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
