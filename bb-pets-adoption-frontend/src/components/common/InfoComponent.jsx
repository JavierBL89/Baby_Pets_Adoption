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
const InfoComponent = ({ /* The `sections`, `title`, and `id` are props being passed to the
`InfoComponent` functional component in React. Here is what each prop is
used for: */
    sections, title, id }) => {

    return (
        <Container id={`${id}_wrapper`} className="" >

            <Container id={`${id}_container`} className="" >
                <Heading tagName="h4" id={`${id}_heading`} className="" text={title} />

                <Container id={`${id}_holder`} className="info_holder" >

                    {
                        sections.map((section, sectionIndex) => {
                            return (
                                <div key={sectionIndex}>
                                    {section.heading && (
                                        <Heading key={`${sectionIndex}-heading`} tagName="h4" id="" text={section.heading} ></Heading>
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
                </Container>
            </Container >
        </Container>
    )
};

export default InfoComponent;