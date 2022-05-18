import {VDetailInfo} from "./VDetailInfo";
import React, {useContext, useEffect, useState} from "react";
import {UserContext} from "../../../../../context/userContext";

export function CDetailInfo() {
    const context = useContext(UserContext);
    const [_, rerun] = useState(new class{});

    useEffect(async () => {
        if (context.token){
            // console.log(context.info);
        }
    }, [context, _])

    return <VDetailInfo
        refresh={() => rerun(new class{})}
    />
}