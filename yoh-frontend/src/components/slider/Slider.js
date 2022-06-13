import React, {useEffect, useState} from "react";
import "./Slider.css";
import {RightArrow} from "../arrows/RightArrow/RightArrow";
import {LeftArrow} from "../arrows/LeftArrow/LeftArrow";
import FlipMove from 'react-flip-move';

export function Slider(props) {
    const [view, setView] = useState([]);
    const [start, setStart] = useState(0);

    useEffect(() => {
        for (let index = 0; index < props.children.length; index++){
            view.push({html: props.children[index], id: index})
        }
    }, [])

    return (
        <div className={"slider"}>
            {start !== 0 ? <LeftArrow onClick={() => setStart(start - 1)}/> : null}
            <FlipMove className={"slider-move"}>
                {
                    view.slice(start, start + props.max).map((element) => {
                        return (
                            <div key={element.id}>
                                {element.html}
                            </div>
                        )
                    })
                }
            </FlipMove>
            {
                start + props.max < props.children.length ?
                    <RightArrow onClick={() => setStart(start + 1)}/> : null
            }
        </div>
    )
}