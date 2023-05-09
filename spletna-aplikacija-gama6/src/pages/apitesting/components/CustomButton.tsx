import React, { useState } from 'react';
import classNames from 'classnames';
import { FunctionComponent } from 'react'

interface CustomButtonProps {
    label: string;
    isSelected: boolean;
    onClick: () => void;
}

const CustomButton: React.FC<CustomButtonProps>  = ({label,isSelected,onClick}) => {

    const btnClass = classNames({
        'rounded-xl bg-pink-500 px-4 py-2 font-semibold hover:bg-white hover:text-pink-500':true,
        'bg-white': isSelected,
        'text-pink-500': isSelected,
        'text-white': !isSelected,
    })

    return (
        <button
            onClick = {onClick}
            className={btnClass}
        >
            {label}
        </button>
    )



}

export default CustomButton;