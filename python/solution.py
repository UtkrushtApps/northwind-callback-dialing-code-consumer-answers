import requests

API_BASE = "https://jsonmock.hackerrank.com/api/countries"


def fetch_country(country: str) -> dict:
    """Query the upstream country service for the given country name.

    Returns the parsed JSON body as a dict. Tests monkeypatch this function
    so the committed test never reaches the network.
    """
    response = requests.get(API_BASE, params={"name": country})
    return response.json()


def getPhoneNumbers(country: str, phoneNumber: str) -> str:
    """Resolve the country's calling code and return "+<Calling Code> <Phone Number>".

    Query the country service via fetch_country(country). The JSON body has a
    "data" field that is an array: exactly one record when the country is
    found, empty when it is not. A record has "name" (str) and "callingCodes"
    (list of str). When callingCodes has more than one entry, use the one at
    the HIGHEST index. Build the result as a plus sign, the calling code, one
    space, then phoneNumber unchanged. When data is empty, return the literal
    string "-1".
    """
    body = fetch_country(country)
    data = body.get("data", []) if isinstance(body, dict) else []

    if not data:
        return "-1"

    first_record = data[0]
    calling_codes = first_record.get("callingCodes", []) if isinstance(first_record, dict) else []

    if not calling_codes:
        return "-1"

    calling_code = calling_codes[-1]
    return f"+{calling_code} {phoneNumber}"
