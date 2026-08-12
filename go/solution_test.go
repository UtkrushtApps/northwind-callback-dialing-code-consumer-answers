package dialingcode

import "testing"

func stub(resp CountryResponse) Fetcher {
	return func(country string) (CountryResponse, error) {
		return resp, nil
	}
}

func record(name string, codes ...string) CountryResponse {
	var r CountryResponse
	r.Data = append(r.Data, struct {
		Name         string   `json:"name"`
		CallingCodes []string `json:"callingCodes"`
	}{Name: name, CallingCodes: codes})
	return r
}

func TestSingleCallingCode(t *testing.T) {
	Fetch = stub(record("Afghanistan", "93"))
	if got := GetPhoneNumbers("Afghanistan", "656445445"); got != "+93 656445445" {
		t.Fatalf("expected +93 656445445, got %q", got)
	}
}

func TestMultipleCallingCodesUsesHighestIndex(t *testing.T) {
	Fetch = stub(record("Puerto Rico", "1787", "1939"))
	if got := GetPhoneNumbers("Puerto Rico", "564593986"); got != "+1939 564593986" {
		t.Fatalf("expected +1939 564593986, got %q", got)
	}
}

func TestNotFoundReturnsMinusOne(t *testing.T) {
	Fetch = stub(CountryResponse{})
	if got := GetPhoneNumbers("Oceania", "987574876"); got != "-1" {
		t.Fatalf("expected -1, got %q", got)
	}
}
