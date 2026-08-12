const API_BASE = 'https://jsonmock.hackerrank.com/api/countries';

// Retrieves the upstream JSON body for a country name.
// Tests override this export so the committed test never reaches the network.
async function fetchCountry(country) {
  const res = await fetch(`${API_BASE}?name=${encodeURIComponent(country)}`);
  return res.json();
}

/**
 * Resolve the country's calling code and return "+<Calling Code> <Phone Number>".
 * Query the country service via fetchCountry(country). The JSON body has a
 * "data" array holding exactly one record when the country is found and empty
 * when it is not. A record has "name" and "callingCodes" (array of strings).
 * When callingCodes has more than one entry, use the one at the highest index.
 * When data is empty, return the literal string "-1".
 *
 * @param {string} country
 * @param {string} phoneNumber
 * @returns {Promise<string>}
 */
async function getPhoneNumbers(country, phoneNumber) {
  const body = await module.exports.fetchCountry(country);
  const data = body && Array.isArray(body.data) ? body.data : [];

  if (data.length === 0) {
    return '-1';
  }

  const callingCodes = Array.isArray(data[0].callingCodes) ? data[0].callingCodes : [];
  if (callingCodes.length === 0) {
    return '-1';
  }

  const callingCode = callingCodes[callingCodes.length - 1];
  return `+${callingCode} ${phoneNumber}`;
}

module.exports = { getPhoneNumbers, fetchCountry };
