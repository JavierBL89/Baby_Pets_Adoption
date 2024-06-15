import React from "react";

/**
 * Component to dinamically render heaidng HTML elements
 * 
 * - Uses a switch statement to check against the prop passes 'tagName' e.g "h1", "h2", "h3"..
 * 
 * @param {*} id - the element unique identifier
 * @param {*} tagName - the tag name
 * @param {*} text - the text content
 * @param {*} className - the class name
 * @returns the Heading component element
 **/
const Heading = ({ tagName, text, id, className }) => {

    let HeadingTag;


    switch (tagName) {
        case "h1":
            HeadingTag = tagName;
            break;
        case "h2":
            HeadingTag = tagName;
            break;
        case "h3":
            HeadingTag = tagName;
            break;
        case "h4":
            HeadingTag = tagName;
            break;
        case "h5":
            HeadingTag = tagName;
            break;
        case "h6":
            HeadingTag = tagName;
            break;

        default:
            return null;
    }

    return (<HeadingTag id={id} className={className}>{text}</HeadingTag>);

};

export default Heading;