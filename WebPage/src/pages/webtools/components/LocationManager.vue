<template>
  <a-modal v-model:visible="DelAllvisible" @ok="DelAllhandleOk" @cancel="handleCancel">
    <template #title>
      提示
    </template>
    <div>确定要删除全部标记点吗？</div>
  </a-modal>

  <a-modal v-model:visible="Addvisible" @ok="AddhandleOk" @cancel="handleCancel">
    <template #title>
      添加传送点
    </template>
    <a-form-item field="name" label="名称">
      <a-input v-model="form.name" placeholder="请输入名称，如果存在请先删除" />
    </a-form-item>
  </a-modal>


  <div class="LocationManager">
    <a-space direction="vertical">
      <a-button type="outline" @click="updateTransfer">刷新</a-button>
      <a-button type="outline" @click="addTransfer">添加</a-button>
      <a-button type="outline" @click="delAllTransfer">全部删除</a-button>
      <a-table :columns="columns" :data="appStore.TPMList">
        <template #optional="{ record }">
          <a-button @click="handle(record)">操作</a-button>
        </template>
      </a-table>
    </a-space>
  </div>
  <a-drawer :width="500" :visible="visible" placement="right" @ok="handleOk" @cancel="handleCancel" unmountOnClose>
    <template #title>
      操作 传送点: {{ Name }}
    </template>
    <div class="LocationManager-drawer">
      <a-space direction="vertical" fill>
        <div>
          一键操作
        </div>
        <a-space wrap>
          <a-button v-for="(item, index) in operationData" type="outline" :key="index" @click="handleOP(item.value)">{{
              item.label
          }}</a-button>
        </a-space>
      </a-space>
    </div>
  </a-drawer>
</template>
<script setup lang="ts">
import {reactive , ref, onMounted } from 'vue'

import { useAppStore } from '@/store/modules/app'
const appStore = useAppStore()

import operationLocationdata from './json/operationLocation.json'


function updateTransfer() {
  appStore.socketSend("{\"type\":\"TPMList\",\"data\":\"0\"}");
}

function addTransfer() {
  Addvisible.value = true;
}
function delAllTransfer() {
  DelAllvisible.value = true;
}
const data = ref([])
const columns = [
  {
    title: '传送点名称',
    dataIndex: 'Name',
  },
  {
    title: '坐标',
    dataIndex: 'pos',
  },
  {
    title: '场景',
    dataIndex: 'SceneId',
  },
  {
    title: 'UID',
    dataIndex: 'uid',
  },
  {
    title: '操作',
    slotName: 'optional'
  },
];

const operationData = ref(operationLocationdata)
const Name = ref(1)
const DelAllvisible = ref(false)
const DelAllhandleOk = () => {
  send("tpm delall yes");
  DelAllvisible.value = false;
};

const Addvisible = ref(false)
const AddhandleOk = () => {
  send("tpm add " + form.name);
  Addvisible.value = false;
};

const form = reactive({
  name: ''
})


const handle = (record: any) => {
  Name.value = record.Name;
  visible.value = true;
}
const handleOP = (valueList: string[]) => {
  for (let index = 0; index < valueList.length; index++) {
    const element = valueList[index];
    var i = element.replace("@", Name.value + "");
    send(i);
    updateTransfer();
  }

}
function send(cmd: string) {
  const send_msg = {
    type: 'CMD',
    data: cmd,
  }
  const send_msg_str = JSON.stringify(send_msg)
  appStore.socketSend(send_msg_str)
  visible.value = false;
}

const handleOk = () => {
  visible.value = false;
}
const handleCancel = () => {
  visible.value = false;
}
const visible = ref(false);


onMounted(() => {
  updateTransfer()
})

</script>
<style lang="less" scoped>
.LocationManager {
  margin-top: 20px;
  width: 700px;
}
</style>
<style lang="less" >
.LocationManager-drawer {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>

