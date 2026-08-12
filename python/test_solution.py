import solution
from solution import getPhoneNumbers


def _stub(payload):
    def _fetch(country):
        return payload
    return _fetch


def test_single_calling_code(monkeypatch):
    monkeypatch.setattr(
        solution,
        "fetch_country",
        _stub({"data": [{"name": "Afghanistan", "callingCodes": ["93"]}]}),
    )
    assert getPhoneNumbers("Afghanistan", "656445445") == "+93 656445445"


def test_multiple_calling_codes_uses_highest_index(monkeypatch):
    monkeypatch.setattr(
        solution,
        "fetch_country",
        _stub({"data": [{"name": "Puerto Rico", "callingCodes": ["1787", "1939"]}]}),
    )
    assert getPhoneNumbers("Puerto Rico", "564593986") == "+1939 564593986"


def test_not_found_returns_minus_one(monkeypatch):
    monkeypatch.setattr(solution, "fetch_country", _stub({"data": []}))
    assert getPhoneNumbers("Oceania", "987574876") == "-1"
