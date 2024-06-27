import React from "react";


/***
 * 
 */
const ButtonComponent = ({ href, onClick, type, className, id, text, disabled }) => {

    return (
        <button href={href} onClick={(e) => onClick()} type={type} className={className} id={id} disabled={disabled} >{text} </button>
    );

};


export default ButtonComponent;