import { createContext, Dispatch, ReactNode, Reducer, useReducer } from "react"
import { ICartItem } from "@interfaces"

interface CartType {
    cartItems: ICartItem[],
    dispatchCartItems: Dispatch<Action>
}

interface Action {
    type: string,
    payload: ICartItem[] | ICartItem
}

const CART_ACTION = {
    CREATE: "create",
    DELETE: "delete",
    UPDATE: "update"
}

const CartContext = createContext<CartType>({} as CartType)

const CartReducer: Reducer<ICartItem[], Action> = (state, action) => {
    switch (action?.type) {
        case CART_ACTION.CREATE:
            return action.payload as ICartItem[]
        case CART_ACTION.UPDATE:
            return state.map((v)=>{
                const cartItem = action.payload as ICartItem
                if (cartItem.id === v.id) return cartItem
                return v
            })
        case CART_ACTION.DELETE:
            return state.filter(v => {
                const cartItem = action.payload as ICartItem
                return cartItem.id !== v.id   
            })
        default:
            return state
    }
}

const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [cartItems, dispatchCartItems] = useReducer(CartReducer, [])

    return (
        <CartContext.Provider value={{ cartItems, dispatchCartItems } as CartType}>
            {children}
        </CartContext.Provider>
    )
}

export {
    CartContext, CartProvider, 
    // eslint-disable-next-line react-refresh/only-export-components
    CART_ACTION
}