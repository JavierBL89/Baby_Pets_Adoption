import React from "react";


/***
 * Component to represent a pargraph HTML elemnt
 */
const TextComponent = ({ text, id, className }) => {

    return (
        <p id={id} className={className}>
            {text}
        </p>
    )

};


export default TextComponent;