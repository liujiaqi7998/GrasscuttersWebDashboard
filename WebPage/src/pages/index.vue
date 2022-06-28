<script lang="ts">

import {ref} from 'vue'

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

function Go_old() {
  window.location.href = './webtools_old.html?server=' + ServerAddress.value + '&key=' + ServerKey.value
}

</script>

<template>
  <Header/>
  <div class="ct">
    <div class="title">
      WebTools
    </div>
    <div class="introduce">
      割草机网页工具箱
    </div>
    <div class="start">
      <router-link to="/webtools/login" class="mr-3 flex-none w-[3.0625rem] md:w-auto leading-6 dark:text-slate-200">
        点我开始吧
      </router-link>
    </div>
  </div>
</template>


<style lang="less">
.ct {
  width: 500px;
  margin: 20vh auto auto;

  .title {
    text-align: center;
    font-size: 28px;
  }

  .introduce {
    margin-top: 10px;
    color: #545343;
    text-align: center;
    font-size: 16px;
  }

  .start {
    margin-top: 10px;
    text-align: center;
    font-size: 24px;
  }
}
</style>