import { UserContext, CartContext } from "@contexts";
import { useContext, useEffect, useState } from "react";

export const useUserContext = ()=>useContext(UserContext)
export const useCartContext = ()=>useContext(CartContext)

export const useDebounce = <T>(value: T, delay: number = 500) => {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const handler = setTimeout(() => setDebouncedValue(value), delay);
    return () => clearTimeout(handler);
  }, [value, delay]);

  return debouncedValue;
}