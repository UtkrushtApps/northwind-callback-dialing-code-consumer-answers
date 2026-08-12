package dialingcode

import (
	"encoding/json"
	"io"
	"net/http"
	"net/url"
)

const apiBase = "https://jsonmock.hackerrank.com/api/countries"

// CountryResponse mirrors the upstream JSON body.
type CountryResponse struct {
	Data []struct {
		Name         string   `json:"name"`
		CallingCodes []string `json:"callingCodes"`
	} `json:"data"`
}

// Fetcher retrieves the upstream response for a country name.
// Tests inject a stub so the committed test never reaches the network.
type Fetcher func(country string) (CountryResponse, error)

// DefaultFetcher queries the live country service.
var DefaultFetcher Fetcher = func(country string) (CountryResponse, error) {
	var out CountryResponse
	resp, err := http.Get(apiBase + "?name=" + url.QueryEscape(country))
	if err != nil {
		return out, err
	}
	defer resp.Body.Close()
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return out, err
	}
	if err := json.Unmarshal(body, &out); err != nil {
		return out, err
	}
	return out, nil
}

// Fetch is the seam GetPhoneNumbers calls; tests swap it for a stub.
var Fetch = DefaultFetcher

// GetPhoneNumbers resolves the country's calling code and returns
// "+<Calling Code> <Phone Number>". The upstream "data" array holds exactly
// one record when the country is found and is empty when it is not. When
// callingCodes has more than one entry, use the one at the highest index.
// When data is empty, return the literal string "-1".
func GetPhoneNumbers(country, phoneNumber string) string {
	response, err := Fetch(country)
	if err != nil || len(response.Data) == 0 {
		return "-1"
	}

	callingCodes := response.Data[0].CallingCodes
	if len(callingCodes) == 0 {
		return "-1"
	}

	callingCode := callingCodes[len(callingCodes)-1]
	return "+" + callingCode + " " + phoneNumber
}
