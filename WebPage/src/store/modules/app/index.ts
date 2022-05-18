import {defineStore} from 'pinia'
import piniaStore from '@/store/index'
import {AppState} from './types';
import {useWebSocket} from '@vueuse/core'
import {watch} from 'vue'
import {Message} from '@arco-design/web-vue'

export const useAppStore = defineStore(
    // 唯一ID
    'app',
    {
        state: () => ({
            title: "",
            h1: '',
            theme: 'dark',
            isLogin: false,
            send: (data: string | ArrayBuffer | Blob, useBuffer?: boolean | undefined) => {
            },
            close: () => {
            },
            open: () => {
            },
            mesgData: [""],
            PlayerList: [],
            TPMList: []
        }),
        getters: {},
        actions: {
            // Change theme color
            toggleTheme(dark: boolean) {
                if (dark) {
                    this.theme = 'dark';
                    document.documentElement.classList.add('dark');
                    document.body.setAttribute('arco-theme', 'dark');
                    localStorage.setItem('theme', this.theme)
                } else {
                    this.theme = 'light';
                    document.documentElement.classList.remove('dark');
                    document.body.removeAttribute('arco-theme');
                    localStorage.setItem('theme', this.theme)
                }
            },
            socketConnect(wss: string) {

                const {status, data, send, open, close} = useWebSocket(wss, {
                    autoReconnect: {
                        retries: 2,
                        delay: 1000,
                        onFailed() {
                            Message.error('连接失败')
                        },
                    },
                    //心跳
                    heartbeat: {
                        message:"{\"type\":\"Player\",\"data\":\"0\"}",
                        interval: 5000,
                    },
                    onConnected: (ws) => {
                        this.isLogin = true
                        console.log("已登录");
                        ws.send("{\"type\":\"TPMList\",\"data\":\"0\"}");
                        ws.send("{\"type\":\"Player\",\"data\":\"0\"}");
                    }
                })
                watch(
                    data,
                    (parse) => {
                        parse = JSON.parse(parse)
                        switch (parse.eventName) {
                            case "cmd_msg":
                                this.mesgData.push(parse.data)
                                Message.info(parse.data)
                                break;
                            case "PlayerList":
                                this.PlayerList = parse.data
                                break;
                            case "TPMList":
                                this.TPMList = parse.data
                                break;
                            case "success":
                                Message.info(parse.data)
                                break;
                            case "error":
                                Message.error(parse.data)
                                break;
                            case "auth":
                                Message.info(parse.data)
                                break;
                            default:
                                break;
                        }
                    }
                )
                this.send = send
                this.close = close
                this.open = open
            },
            socketSend(str: string) {
                //判断连接状态
                if(this.isLogin){
                    this.send(str)
                }else{
                    Message.error("未连接，请从邮件重新进入")
                }
            },
            socketClose() {
                Message.info("已断开连接")
                this.isLogin = false
                this.close()
            }
        }
    }
)

export function useAppOutsideStore() {
    return useAppStore(piniaStore)
}