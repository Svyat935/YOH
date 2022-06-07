import React from "react";
import {ButtonA} from "../buttons/ButtonA/ButtonA";
import {ButtonC} from "../buttons/ButtonC/ButtonC";
import {LeftArrow} from "../arrows/LeftArrow/LeftArrow";
import {RightArrow} from "../arrows/RightArrow/RightArrow";
import {ButtonAStat} from "../buttons/ButtonAStat/ButtonAStat";

export function Pagination(props) {
    const width_ = "100px";

    const count = props.count,
        start = props.start,
        limit = props.limit-1;
    const pages = count < limit ? 1 : Math.floor(count  / limit),
        currentPage = Math.floor(start / limit) + 1;

    const changePage = (currentPage) => {
        let new_start = (currentPage - 1) * limit;
        console.log(new_start);
        props.setStart(new_start);
        props.refresh();
    }

    const createButtons = () => {
        let view = [];

        view.push(<LeftArrow onClick={() => {
            if (currentPage !== 1){
                props.setStart(start - 9);
                props.refresh();
            }
        }}/>);

        if (count > limit){
            if (currentPage === 1){
                view.push(<ButtonC width={width_} text={"1"}/>);
                view.push(<ButtonA width={width_} text={"2"} onClick={() => changePage(2)}/>);
                view.push(<ButtonA width={width_} text={"3"} onClick={() => changePage(3)}/>);
                view.push(<ButtonAStat width={width_} text={""}/>);
                view.push(<ButtonAStat width={width_} text={""}/>);
                view.push(<ButtonAStat width={width_} text={""}/>);
            }
            else {
                view.push(<ButtonA width={width_} text={"1"}  onClick={() => changePage(1)}/>);
            }

            if (currentPage !== 1 && currentPage !== pages){
                if (currentPage === 2){
                    view.push(<ButtonC width={width_} text={"2"}/>);
                    view.push(<ButtonA width={width_} text={"3"} onClick={() => changePage(3)}/>);
                    view.push(<ButtonA width={width_} text={"4"} onClick={() => changePage(4)}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                }else if (currentPage === 3){
                    view.push(<ButtonA width={width_} text={"2"} onClick={() => changePage(2)}/>);
                    view.push(<ButtonC width={width_} text={"3"}/>);
                    view.push(<ButtonA width={width_} text={"4"} onClick={() => changePage(4)}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                }else if (currentPage === pages - 2) {
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonA width={width_} text={pages - 3} onClick={() => changePage(pages - 3)}/>);
                    view.push(<ButtonC width={width_} text={pages - 2}/>);
                    view.push(<ButtonA width={width_} text={pages - 1} onClick={() => changePage(pages)}/>);
                }else if (currentPage === pages - 1){
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonA width={width_} text={pages - 3} onClick={() => changePage(pages - 3)}/>);
                    view.push(<ButtonA width={width_} text={pages - 2} onClick={() => changePage(pages - 2)}/>);
                    view.push(<ButtonC width={width_} text={pages - 1}/>);
                }else{
                    view.push(<ButtonAStat width={width_} text={""}/>);
                    view.push(<ButtonA width={width_} text={currentPage - 1} onClick={() => changePage(currentPage - 1)}/>);
                    view.push(<ButtonC width={width_} text={currentPage}/>);
                    view.push(<ButtonA width={width_} text={currentPage + 1} onClick={() => changePage(currentPage + 1)}/>);
                    view.push(<ButtonAStat width={width_} text={""}/>);
                }
            }

            if (currentPage === pages){
                view.push(<ButtonAStat width={width_} text={""}/>);
                view.push(<ButtonAStat width={width_} text={""}/>);
                view.push(<ButtonAStat width={width_} text={""}/>);
                view.push(<ButtonA width={width_} text={pages - 2} onClick={() => changePage(pages - 2)}/>);
                view.push(<ButtonA width={width_} text={pages - 1} onClick={() => changePage(pages - 1)}/>);
                view.push(<ButtonC width={width_} text={pages}/>);
            }
            else{
                view.push(<ButtonA width={width_} text={pages} onClick={() => changePage(pages)}/>);
            }
        }else{
            view.push(<ButtonA width={width_} text={"1"}  onClick={() => changePage(1)}/>);
        }


        view.push(<RightArrow onClick={() => {
            if (currentPage !== pages){
                props.setStart(start + 9);
                props.refresh();
            }
        }}/>);

        return view;
    }

    return (
        <div style={{
            display: "flex",
            justifyContent: "space-around"
        }}>
            {createButtons()}
        </div>
    )
}