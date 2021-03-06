import React from "react";
import "./FilterBlock.css";

export function FilterBlock(props) {
    const createFilterList = () => {
        let filterList = props.filters,
            view = [];

        if (filterList !== undefined){
            filterList.forEach((filterOne) => {
                view.push(
                    <div key={filterOne["text"]} className={"filter-one"}>
                        <input
                            id={filterOne["text"]}
                            name={"filterName"}
                            type={"radio"}
                            value={filterOne["value"]}
                            onClick={filterOne["onClick"]}
                            defaultChecked={filterOne["defaultChecked"]}
                        />
                        <label htmlFor={filterOne["text"]}>{filterOne["text"]}</label>
                    </div>
                )
            })
        }
        return view;
    }

    return (
        <div className={"filter-block"}>
            <h3>Сортировка</h3>
            <div className={"filter-list"}>
                {createFilterList()}
            </div>
        </div>
    )
}