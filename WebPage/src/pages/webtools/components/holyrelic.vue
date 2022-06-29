
<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { useClipboard } from '@vueuse/core'
import holyrelicname from './json/holyrelicname.json'
import holyrelicnmain from './json/holyrelicnmain.json'
import holyrelicx from './json/holyrelicnx.json'
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

var holyrelicnamevalue = ref('')
var holyrelicnmainvalue = ref('')

var grade = ref(1)
var num = ref()

const value = computed(() => {
  var xct = ''
  options3.value.forEach((k) => {
    if (k.isCheck) {
      xct = xct + ` ${k.value},${k.num}`
    }
  })
  return `give ${holyrelicnamevalue.value} lv${grade.value + 1} ${holyrelicnmainvalue.value}${xct} `
})
const options = reactive(holyrelicname)

const options2 = reactive(holyrelicnmain)

var holyrelicx1 = holyrelicx.map((k) => {
  const obj = {
    isCheck: false,
    num: 1,
    label: k.label,
    value: k.value,
  }
  return obj
})
const options3 = ref(holyrelicx1)

const message = Message

function copyvalue() {
  send(value.value);
}
</script>

<template>
  <div class="commuse">

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 圣遗物: </div>
      <a-cascader
        allow-search
        v-model="holyrelicnamevalue"
        :options="options"
        placeholder="请输入物品"
        filterable
      />
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 主属性: </div>
      <a-cascader
        allow-search
        v-model="holyrelicnmainvalue"
        :options="options2"
        placeholder="请输入主属性"
        filterable
      />
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 属性小词条: </div>

      <div class="smallho">
        <div class="smallho-item" v-for="(item, index) in options3" :key="index">
          <a-checkbox v-model="item.isCheck"></a-checkbox>
          <div class="text-slate-900 dark:text-slate-100">{{ item.label }} </div>
          <div>
            <a-input-number placeholder="" v-model="item.num" :min="1" />
          </div>
        </div>
      </div>
    </div>

    <div class="commuse-item">
      <div class="text-slate-900 dark:text-slate-100"> 强化等级: </div>

      <a-input-number placeholder="" v-model="grade" :min="1" :max="20" />
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

.smallho {
  height: 300px;
  width: 100%;
  overflow-y: auto;
  .smallho-item {
    margin: 10px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 90%;
    > div {
      &:nth-child(3) {
        width: 80px;
      }
    }
  }
}
</style>
