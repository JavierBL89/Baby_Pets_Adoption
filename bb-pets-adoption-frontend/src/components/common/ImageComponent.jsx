import React from "react";



/***
 * 
 */
const ImageComponent = ({ src, className, alt }) => {

    return (
        <img src={src} className={className} alt={alt} />
    )

};

export default ImageComponent;