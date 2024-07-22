import React, { useState, useContext, } from "react";
import petTagsData from "../../../../petTagsData";
import { Container, Row, Col, Tab, Tabs, Button } from "react-bootstrap";
import PetTag from "./PetTag";
import { DataPetContext } from "../../../../context/DataPetContext";
import { IoIosRemoveCircle } from "react-icons/io";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";


/***
 * PetTagsHolder Component
 * 
 * This component is responsible for managing and displaying the pet tags and handling the pet search based on selected tags 
 * It uses the 'useFetchPetsByTags' custom hook to fetch pet data filtered by tags and leverages the 'DataPetContext' 
 * to manage the state of the pet data globally.
 * 
 * The component contains the following functionalities:
 *  - Displaying a list of tags that can be selected or removed
 *  - Handling the tag selection and removal
 *  - Handling the search action to fetch pets based on selected tags
 * 
 * Props:
 *  - petCategory (string): The category of pets to be displayed (e.g., "kitties" or "puppies").
 * 
 * State:
 *  - selectedTags (array): A list of currently selected tags.
 *  - tagsList (array): A list of tags to be used for fetching pets.
 * 
 * Context:
 *  - DataPetContext: Used to reset the pets data when a new search is performed.
 * 
 * Hooks:
 *  - useFetchPetsByTags: Custom hook to fetch pets based on selected tags.
 * 
 * Usage:
 *  <PetTagsHolder petCategory="kitties" />
 */
const PetTagsHolder = () => {

    const [selectedTags, setSelectedTags] = useState([]);
    const [tagsMessage, setTagsMessage] = useState("");
    const { setTagsList, resetPetsData } = useContext(DataPetContext);
    const { disabled, setDisabled } = useState(false);


    /***
     * Method handles tag selecction.
     * 
     * When user clicks on a tag, this method checks whether if it exists already in the list of selected tags
     *  to avoid duplicates, or pushes the new tag into the list.
     * 
     * @params title - the tag title
     */
    const handleClick = (title) => {

        if (selectedTags.length > 4) {
            setTagsMessage("You have reached the maximu of tags.");

            return;
        }
        // check if new tag is already in the list
        if (selectedTags.includes(title.toLowerCase())) {
            return;
        } else {
            setSelectedTags((prevTags) => [...prevTags, title.toLowerCase()]);
        }
    }

    /***
     * Method handles seacrh submission.
     * 
     * Resets or clears the pets data existing in the 'PetDataContext'.
     * Sets the 'tagList' to be passed down to 'useFetchPetsByTags' hook 
     * which is listeing to changes on it to execute GET request
     * 
     */
    const handleSearch = () => {
        resetPetsData();    // reset pets data state to empty any existing list
        setTagsList([...selectedTags]);  // set tags list with the chosen tags
    }


    /***
     * Method removes a tag from the 'selectedTags'list
     * 
     * Uses filter() to filter out the list removing the selected tag, 
     * and sets 'selectedTags' with new list data.
     * 
     * @params tag - the tag to be removed
     */
    const handleRemoveTag = (tag) => {
        if (selectedTags.length === 5) {
            setTagsMessage("");
        }
        setSelectedTags(selectedTags.filter((t) => t !== tag));
    }

    return (

        <Container id="pet_tags_wrapper">
            <Container id="pet_tags_container">
                <Row flex>
                    <Tabs
                        defaultActiveKey="profile"
                        id="uncontrolled-tab-example"
                        className="mb-3"
                    >
                        {

                            petTagsData.map((category, tagIndex) => {
                                return (
                                    <Tab key={tagIndex} eventKey={`${category.name}`} title={`${category.name}`}>

                                        <Row className=" " id="badge_tag_holder">
                                            {
                                                category.items && category.items.map((item, itemIndex) => {
                                                    return (
                                                        <Col key={itemIndex} xs={4} id={`subtag_${itemIndex}`} className="tag_column">
                                                            <PetTag disabled={disabled}
                                                                onClick={handleClick} title={item} />
                                                        </Col>
                                                    )
                                                })
                                            }
                                        </Row>
                                    </Tab>)
                            })
                        }

                    </Tabs>
                </Row>
                <Row id="selected_tags_holder">
                    <Col xs={9}>
                        <Row className="d-flex flex-row">
                            {
                                selectedTags && selectedTags.length > 0 ? (
                                    selectedTags.map((att, attIndex) => {
                                        return (<Col key={attIndex} >
                                            <IoIosRemoveCircle onClick={() => handleRemoveTag(att)} />

                                            <p className="selected_tag">{att}</p>
                                        </Col>)
                                    })
                                ) : null
                            }
                        </Row>
                    </Col>
                    <Col xs={3}>
                        {
                            selectedTags && selectedTags.length > 0 &&
                            <ButtonComponent id="search_button" onClick={handleSearch} text="Find Pets" />
                        }
                    </Col>
                </Row>
                <Row>
                    {tagsMessage && tagsMessage.length > 0 &&
                        <TextComponent className="text-center" text={tagsMessage} />
                    }
                </Row>
            </Container>

        </Container>
    );

};


export default PetTagsHolder;