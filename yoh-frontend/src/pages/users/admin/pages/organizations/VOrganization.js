import {SearchFrame} from "../../../../../frame/SearchFrame/SearchFrame";
import React from "react";
import {AdminNav} from "../../../../../components/navigate/NavPanel/Admin/AdminNav";

export function VOrganization(props) {
    let filterList = [
        {"text": "По алфавиту", "value": 1, "onClick": () => {}},
        {"text": "По дате", "value": 2, "onClick": () => {}},
    ]

    const createOrganizationsView = () => {
        let orgs = props.organizations,
            view = [];

        for (let org of orgs){
            view.push(

            )
        }
    }

    return <SearchFrame
        navPanel={<AdminNav context={props.context}/>}
        filterList={filterList}
        modalOnHide={null}
        modalTitle={null}
        modalBody={null}
        modalFooter={null}
        title={null}
        buttons={null}
        searchInputOnKeyDown={null}
        searchInputOnBlur={null}
        searchInputOnClick={null}
        content={null}
        contentFooter={null}
    />
}