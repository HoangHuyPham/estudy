import type { IUserData } from '@/interface'
import { create } from 'zustand'

type AppState = {
    lang: 'en' | 'vi',
    userData: null | IUserData
}

type AppActions = {
    setLang: (lang: 'en' | 'vi') => void
    setUserData: (userData: IUserData | null) => void
}

export const useAppStore = create<AppState & AppActions>((set) => ({
    lang: 'vi',
    userData: null,
    setLang: (lang) => set({ lang }),
    setUserData: (userData) => set({ userData }),
}))