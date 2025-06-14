import { createContext, Dispatch, ReactNode, Reducer, useReducer } from "react"
import { IUser } from "@interfaces"

interface UserType {
    user: IUser,
    dispatchUser: Dispatch<Action>
}

interface Action {
    type: string,
    payload: IUser | null
}

const USER_ACTION = {
    ADD: "add",
    DELETE: "delete",
    UPDATE: "update"
}

const UserContext = createContext<UserType>({} as UserType)

const UserReducer: Reducer<IUser | null, Action> = (state, action) => {
    switch (action.type) {
        case USER_ACTION.ADD:
            return {...action.payload} as IUser
        case USER_ACTION.DELETE:
            localStorage.removeItem("jwt")
            return null
        case USER_ACTION.UPDATE:
            return {...state, ...action.payload}
        default:
            return state
    }
}


const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [user, dispatchUser] = useReducer(UserReducer, null)

    return (
        <UserContext.Provider value={{ user, dispatchUser } as UserType}>
            {children}
        </UserContext.Provider>
    )
}

export {
    UserContext, UserProvider, 
    // eslint-disable-next-line react-refresh/only-export-components
    USER_ACTION
}