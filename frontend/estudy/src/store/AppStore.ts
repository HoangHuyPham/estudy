import type { IUserData } from '@/interface'
import { create } from 'zustand'
import { devtools } from 'zustand/middleware'

type AppState = {
    lang: 'en' | 'vi',
    userData?: IUserData
}

type AppActions = {
    setLang: (lang: 'en' | 'vi') => void
    setUserData: (userData: IUserData) => void
}

export const useAppStore = create<AppState & AppActions>()(devtools((set) => ({
    lang: 'vi',
    userData: undefined,
    setLang: (lang) => set({ lang }),
    setUserData: (userData) => set({ userData }),
}), { name: 'AppStore' }))