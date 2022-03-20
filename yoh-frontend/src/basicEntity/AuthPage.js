function ViewAuthPage(props) {

}

export function AuthPage() {


    const authorizeOnServer = async (credentials, password) => {
        // TODO Change root
        let response = await fetch("users", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                credentials: credentials,
                password: password,
        })
    }

    return (
        <div><p>AuthPage</p></div>
    );
}