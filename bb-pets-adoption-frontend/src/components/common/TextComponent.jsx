import React from "react";


/***
 * Component to represent a pargraph HTML elemnt
 */
const TextComponent = ({ onClick, text, id, className }) => {

    return (
        <p id={id} onClick={onClick} className={className}>
            {text}
        </p>
    )

};


export default TextComponent;