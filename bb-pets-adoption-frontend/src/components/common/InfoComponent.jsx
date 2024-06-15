import React from "react";
import Heading from "./Heading";
import { Container } from "react-bootstrap";
import TextComponent from "./TextComponent";


/**
* Component host a container to dinamically render structured content for different pages
* Component accept props 'sections',  which is expected to be a list of JSON objects
*
* We use map() to iterate over the 'sections' array passed
* We use && to conditionally render each of the different key-value pairs within each obejct
*
* Here the expected JSON Object structure:
*  { 
*     heading :
*     content:
*     section: {
*          heading:
*          bulletPoints: []
*         }
*  }
*/
const InfoComponent = ({ sections, title, id }) => {

    return (
        <Container id={`${id}_wrapper`} className="p-5" >
            <Heading tagName="h4" id={`${id}_heading`} className="" text={title} />

            {
                sections.map((section, sectionIndex) => {
                    console.log(section);
                    return (
                        <div key={sectionIndex}>
                            {section.heading && (
                                <Heading key={`${sectionIndex}-heading`} type="h4" id="" text={section.heading} ></Heading>
                            )}
                            {
                                section.content && (
                                    <TextComponent key={`${sectionIndex}-content`} type="h4" id="" className="" text={section.content}></TextComponent>
                                )
                            }
                            {
                                section.section && section.section.map((point, pointIndex) => {

                                    return (
                                        <div key={pointIndex}>
                                            <Heading key={`${sectionIndex}-subheading`} type="h5" id="" text={point.heading}></Heading>
                                            <ul>
                                                {
                                                    point.bulletPoints.map((bullet, bulletIndex) => {
                                                        return (<li key={bulletIndex}>{bullet}</li>)
                                                    })
                                                }
                                            </ul>
                                        </div>
                                    )
                                }
                                )
                            }
                        </div >
                    )
                })
            }

        </Container >
    )
};

export default InfoComponent;