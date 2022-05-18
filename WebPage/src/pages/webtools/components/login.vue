<script setup lang="ts">
import {reactive, ref, watch, onMounted} from 'vue'
import Header from '@/components/Header/index.vue'
import {Message} from '@arco-design/web-vue'
import {useAppStore} from '@/store'

const appStore = useAppStore()

function getQueryVariable(variable: string) {
  const query = window.location.href.split("?");
  if (query.length <= 1) {
    return false;
  }
  const vars = query[1].split("&");
  for (let i = 0; i < vars.length; i++) {
    const pair = vars[i].split("=");
    if (pair[0] == variable) {
      return pair[1];
    }
  }
  return false;
}


var ServerAddress = ref('')
var ServerKey = ref('')

var URLServerAddress = getQueryVariable('server')

if (URLServerAddress !== false) {
  //URL解码
  URLServerAddress = decodeURIComponent(URLServerAddress);
  ServerAddress.value = URLServerAddress
  ServerKey.value = getQueryVariable('key').toString()
}


function login() {
  var wss = `${ServerAddress.value}?key=${ServerKey.value}`
  if (wss) {
    if (wss.indexOf("ws://") < 0 && wss.indexOf("wss://") < 0) {
      Message.error(`地址不正确`)
    } else {
      appStore.socketConnect(wss)
    }
  } else {
    Message.error(`地址不正确`)
  }


}

function close() {
  appStore.socketClose()
}
const RegSSLvisible = ref(false)
function regSSL() {
  RegSSLvisible.value = true
}

function RegSSLhandleOk() {
  if (URLServerAddress.toString().indexOf("wss://") < 0) {
    Message.error(`地址不正确`)
  } else {
    var openURL = URLServerAddress.toString().replace("wss://", "https://")
    openURL = openURL.replace("WebTools", "")
    window.open(openURL)
  }
  RegSSLvisible.value = false
}

</script>
<template>

  <a-modal v-model:visible="RegSSLvisible" @ok="RegSSLhandleOk">
    <template #title>
      提示
    </template>
    <div>
      连接失败可能是你没有配置SSL证书导致的，请手动授权
      <br>
      授权方法：点击确定，在弹出的窗口中进行如下操作，看到“欢迎使用 Grasscutter”后返回到本页面重新连接
    </div>
    <a-image
        width="450"
        src="https://s1.ax1x.com/2022/05/18/OotfP0.png"
    />
  </a-modal>

  <div class="login" v-if="!appStore.isLogin">
    <div class="title text-slate-900 dark:text-slate-100">
      未连接到服务器
      <br>
      请发送命令'/wt'获取邮件，并从邮件中获取连接验证码
      <br>
      连接地址为 wss(ws)://IP(域名):端口/WebTools
    </div>
    <div>
      <div class="commuse-item">
        <div class="text-slate-900 dark:text-slate-100"> 连接地址:</div>
        <a-input v-model="ServerAddress" clearable placeholder="请输连接地址"/>
      </div>
      <div class="commuse-item">
        <div class="text-slate-900 dark:text-slate-100"> 验证码:</div>
        <a-input v-model="ServerKey" clearable placeholder="请输入验证码"/>
      </div>
      <div class="title">
        <a-button type="outline" @click="login"> 连接</a-button>
      </div>
      <div class="title">
        <a-button type="outline" @click="regSSL">连接失败请点我，过SSL认证</a-button>
      </div>
    </div>
  </div>
  <div v-else>
    <div id="qrcode"></div>
    <div class="inf">
      <a-descriptions style="margin-top: 20px" size="large" title="已连接" :column="1"/>
    </div>
    <div>
      <a-button type="outline" @click="close"> 断开连接</a-button>
    </div>
  </div>
</template>
<style lang="less" scoped>
.login {
  width: 600px;
  margin-top: 10vh;

  .title {
    text-align: center;
    line-height: 40px;
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
}
</style>