<template>
  <div class="personnel">
    <a-space direction="vertical">
      <a-button type="outline" @click="updatePlayerList">刷新</a-button>
      <a-table :columns="columns" :data="appStore.PlayerList">
        <template #optional="{ record }">
          <a-button @click="handle(record)">操作</a-button>
        </template>
      </a-table>
    </a-space>
  </div>
  <a-drawer :width="500" :visible="visible" placement="right" @ok="handleOk" @cancel="handleCancel" unmountOnClose>
    <template #title>
      操作 UID @{{ handleUid }}
    </template>
    <div class="personnel-drawer">
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
import {  ref, onMounted } from 'vue'
import { useAppStore } from '@/store/modules/app'
import operationdata from './json/operation.json'


const appStore = useAppStore()
function updatePlayerList() {
  appStore.socketSend("{\"type\":\"Player\",\"data\":\"0\"}");
}
const data = ref([])
const columns = [
  {
    title: 'UID',
    dataIndex: 'uid',
  },
  {
    title: '昵称',
    dataIndex: 'nickname',
  },
  {
    title: '签名',
    dataIndex: 'signature',
  },
  {
    title: '等级',
    dataIndex: 'Level',
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
    title: '操作',
    slotName: 'optional'
  },
];

const operationData = ref(operationdata)
const handleUid = ref(1)
const handle = (record: any) => {
  handleUid.value = record.uid
  visible.value = true;
}
const handleOP = (valueList: string[]) => {
  for (let index = 0; index < valueList.length; index++) {
    const element = valueList[index];
    var i = element.search("@") + 1
    send(insertStr(element, i))
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
function insertStr(soure: string, start: number, newStr = handleUid.value) {
  return soure.slice(0, start) + newStr + soure.slice(start);
}
const handleOk = () => {
  visible.value = false;
}
const handleCancel = () => {
  visible.value = false;
}
const visible = ref(false);


onMounted(() => {
  updatePlayerList()
})

</script>
<style lang="less" scoped>
.personnel {
  margin-top: 20px;
  width: 700px;
}
</style>
<style lang="less" >
.personnel-drawer {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>

