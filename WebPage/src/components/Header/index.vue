


<script setup lang="ts">
import SvgIcon from '../SvgIcon/index.vue'
import { ref } from 'vue'
import { useDark } from '@vueuse/core';
import { useAppStore } from '@/store';
import { IconMoonFill, IconSunFill } from '@arco-design/web-vue/es/icon';
import { IconClockCircle } from '@arco-design/web-vue/es/icon';
const title = ref('I want to study typescript')
// 检测浏览器系统主题
const darkThemeMq = window.matchMedia('(prefers-color-scheme: dark)')
const appStore = useAppStore()
const theme = computed(() => {
  return appStore.theme
})
const isDark = useDark({
  selector: 'body',
  attribute: 'arco-theme',
  valueDark: 'dark',
  valueLight: 'light',
  storageKey: 'arco-theme',
  onChanged(dark: boolean) {
    appStore.toggleTheme(dark);
  },
})
const toggleTheme = useToggle(isDark)
const ThemeChange = (val: string | number | boolean) => {
  if (!val) {
    document.documentElement.classList.add('dark')
    document.body.setAttribute('arco-theme', 'dark')
  } else {
    document.body.removeAttribute('arco-theme')
    document.documentElement.classList.remove('dark')
  }
}

const LoginHS = ref(true)

</script>

<template>
  <header class="antialiased bg-white Male text-slate-500 dark:text-slate-400 dark:bg-slate-900">
    <div
      class="sticky top-0 z-40 w-full backdrop-blur flex-none transition-colors duration-500 lg:z-50 lg:border-b lg:border-slate-900/10 dark:border-slate-50/[0.06] bg-white/95 supports-backdrop-blur:bg-white/60 dark:bg-transparent">
      <div class="mx-auto max-w-8xl">
        <div class="px-4 py-4 border-b border-slate-900/10 lg:px-8 lg:border-0 dark:border-slate-300/10">
          <div class="relative flex items-center text-2xl sm:text-2xl font-blimone">
            <router-link to="/" class="mr-3 flex-none w-[2.0625rem] md:w-auto leading-6 dark:text-slate-200">
              WebTools</router-link>
            <div class="relative items-center hidden ml-auto lg:flex">
              <nav class="text-sm font-semibold leading-6 text-slate-700 dark:text-slate-200">
                <ul class="flex space-x-8">
                  <li>
                    <a href="https://wiki.biligame.com/ys/%E9%A6%96%E9%A1%B5" target="_bank"
                      class="block ml-6 text-slate-400 hover:text-slate-500 dark:hover:text-slate-300">
                      Genshin-BiliBili-wiki
                    </a>
                  </li>
                  <li>
                    <a href="https://github.com/wmn1525" target="_bank"
                       class="block ml-6 text-slate-400 hover:text-slate-500 dark:hover:text-slate-300">
                      感谢@wmn1525提供的页面参考
                    </a>
                  </li>

                </ul>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<style lang="less">
.prose {
  color: #334155;
  font-size: 0.875em;
  font-variant-ligatures: none;

  code {
    color: #0f172a;
    font-family: Fira Code VF, ui-monospace, SFMono-Regular, Menlo, Monaco,
      Consolas, Liberation Mono, Courier New, monospace;

    &::before {
      content: "`";
    }

    &::after {
      content: "`";
    }
  }

  &.dark\:prose-dark {
    color: #94a3b8;

    code {
      color: #e2e8f0;
    }
  }
}
</style>