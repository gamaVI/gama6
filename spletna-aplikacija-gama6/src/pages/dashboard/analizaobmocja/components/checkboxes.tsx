"use client"
/* eslint-disable */
import { Checkbox } from "@/components/ui/checkbox"
export function Checkboxes({enabledTypes,setEnabledTypes,types}) {
  return (
    <div className="flex items-start  flex-col">
      {types && types.map((type) => (
        <div className="flex flex-row gap-2 mt-2 ">
       <Checkbox id="terms"
       onClick = {()=>{
          if(enabledTypes.includes(type)){
            setEnabledTypes(enabledTypes.filter((enabledType)=>enabledType!=type))
          }else{
            setEnabledTypes([...enabledTypes,type])
          }
        }}
     />
     <label
       htmlFor="terms"
       className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
     >
        {type}
     </label>
     </div>
      )
      )}
    </div>
  )
}
