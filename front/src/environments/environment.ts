export const environment = {
  production: false,
  keycloak: {
    url: `${window.location.origin.replace(":4200","")}/keycloak`,
    realm: "drive",
    clientId: "drive",
  },
};
