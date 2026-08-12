const solution = require('./solution');

function stub(payload) {
  solution.fetchCountry = async () => payload;
}

describe('getPhoneNumbers', () => {
  test('single calling code', async () => {
    stub({ data: [{ name: 'Afghanistan', callingCodes: ['93'] }] });
    await expect(solution.getPhoneNumbers('Afghanistan', '656445445')).resolves.toBe('+93 656445445');
  });

  test('multiple calling codes uses highest index', async () => {
    stub({ data: [{ name: 'Puerto Rico', callingCodes: ['1787', '1939'] }] });
    await expect(solution.getPhoneNumbers('Puerto Rico', '564593986')).resolves.toBe('+1939 564593986');
  });

  test('not found returns -1', async () => {
    stub({ data: [] });
    await expect(solution.getPhoneNumbers('Oceania', '987574876')).resolves.toBe('-1');
  });
});
