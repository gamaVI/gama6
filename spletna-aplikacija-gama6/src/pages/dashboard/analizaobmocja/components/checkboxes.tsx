"use client"

import { Checkbox } from "@/components/ui/checkbox"
export function Checkboxes({enabledTypes,setEnabledTypes}) {
  return (
    <div className="flex items-center space-x-2">
      <Checkbox id="terms"  
        onClick={()=>{
              if(enabledTypes.includes(1)){
            setEnabledTypes(enabledTypes.filter((item)=>item!==1))
          }else{
            setEnabledTypes([...enabledTypes,1])
          }
        }}
      />
      <label
        htmlFor="terms"
        className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
      >
        Stanovanja
      </label>
      <Checkbox id="terms" 
        onClick={()=>{
      
          if(enabledTypes.includes(2)){
            setEnabledTypes(enabledTypes.filter((item)=>item!==2))
          }else{
            setEnabledTypes([...enabledTypes,2])
          }

        }}

      />
      <label
        htmlFor="terms"
        className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
      >
        Hiše
      </label>
    </div>
  )
}
