import React from "react";


/***
 * 
 */
const ButtonComponent = ({ href, onClick, type, className, id, text }) => {

    return (
        <button href={href} onClick={(e) => onClick()} type={type} className={className} id={id} >{text}</button>
    );

};


export default ButtonComponent;