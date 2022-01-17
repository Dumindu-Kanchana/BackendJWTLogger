package org.gdk.restservice;

public class CreateResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(String status) {
        Response response = new Response();
        response.setStatus(status);
        this.response = response;
    }

    public static class Response {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
