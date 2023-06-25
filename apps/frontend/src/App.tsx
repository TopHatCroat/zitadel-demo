import React from "react";
import { AuthProvider } from "oidc-react";
import "./App.css";
const oidcConfig = {
  onSignIn: async (response: any) => {
    alert(
      "You logged in :" +
      response.profile.given_name +
      " " +
      response.profile.family_name
    );
    console.log(response)
    window.location.hash = "";
  },
  authority: "https://test-fojq5t.zitadel.cloud", // replace with your instance
  clientId: "220120641561493761@demo",
  responseType: "code",
  redirectUri: "http://localhost:3000",
  scope: "openid profile email",
};

function App() {
  return (
    <AuthProvider {...oidcConfig}>
      <div className="App">
        <header className="App-header">
          <p>Hello World</p>
        </header>
      </div>
    </AuthProvider>
  );
}

export default App;
