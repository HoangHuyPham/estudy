// import { Checkbox, CheckboxChangeEvent } from "antd";
import { CartItem } from "@components";
// import { useCartItem, useCheckoutContext } from "../contexts/hooks";
import { CART_ACTION } from "../contexts/CartContext";
import { ICart, ICartItem } from "../interfaces";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
// import { ButtonWarning } from "../custom/button";
// import { del, Endpoint, get, post, update } from "../request/AppRequest";
import { toast } from "react-toastify";
import { useCartContext } from "@hooks";
// import { CHECKOUT_ACTION } from "../contexts/CheckoutContext";

export const CartList: React.FC = () => {
    const { cartItems, dispatchCartItems } = useCartContext()
    // const { dispatchCheckoutItems } = useCheckoutContext()
    const [needLogin, setNeedLogin] = useState(false)
    const navigate = useNavigate()

    useEffect(() => {
        fetchCart()
    }, [])

    const fetchCart = async () => {
        try {
            const { data } = await get({
                url: Endpoint.CART_URL
            })

            if (!data) return null

            dispatchCartItems({
                type: CART_ACTION.CREATE,
                payload: data.cartItems
            })

            setNeedLogin(false)
        } catch (err) {
            if (err?.status === 401)
                setNeedLogin(true)
            else
                toast.error(`Bug!`)
        }
    }

    const removeCartItem = async (itemItem: ICartItem) => {
        const data = await del({
            url: `${Endpoint.CART_URL}/${itemItem.id}`
        })
        if (data?.status === 200) {
            dispatchCartItems({
                type: CART_ACTION.DELETE,
                payload: itemItem
            })
        } else {
            toast.error(`Bug!`)
        }
    }

    const updateCartItem = async (itemItem: ICartItem) => {
        const data = await update({
            url: `${Endpoint.CART_URL}/${itemItem.id}`,
            payload: itemItem
        })
        if (data?.status === 200) {
            dispatchCartItems({
                type: CART_ACTION.UPDATE,
                payload: itemItem
            })
        } else {
            toast.error(`Bug!`)
        }
    }

    const handleCheckout = () => {
        const checkOutItems: ICartItem[] = []
        cartItems.map((i) => {
            if (i.isSelected)
                checkOutItems.push(i)
        })

        dispatchCheckoutItems({
            type: CHECKOUT_ACTION.INIT,
            payload: checkOutItems
        })

        navigate("/checkout")
    }

    return (
        <>
            <div className="flex bg-[#3b82f6] w-[100%] px-5 py-2 m-2 text-1xl shadow-xl animate-fade">
                <span className="w-[40%]">Product</span>
                <span className="w-[20%] text-center">Unit Price</span>
                <span className="w-[10%] text-center">Quantity</span>
                <span className="w-[20%] text-center">Total Price</span>
                <span className="w-[10%] text-center">Action</span>
            </div>

            {cartItems && cartItems.length > 0 &&

                (cartItems?.length) > 0 && cartItems?.map((v) => <CartItem key={v.id} cartItem={v} removeCartItem={removeCartItem} updateCartItem={updateCartItem} />)

                || <p className="text-black">{needLogin && "You need to login to see this." || "No items..."}</p>}

            {
                cartItems &&
                cartItems?.length > 0 &&
                cartItems?.reduce((res, current) => res || current.isSelected, false) &&
                <ButtonWarning onClick={handleCheckout} type="primary">Checkout</ButtonWarning>
            }
        </>
    )
}