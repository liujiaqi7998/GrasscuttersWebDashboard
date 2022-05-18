import { createPinia } from 'pinia';
import { useAppStore } from './modules/app';

const pinia = createPinia();

export { useAppStore };
export default pinia;
