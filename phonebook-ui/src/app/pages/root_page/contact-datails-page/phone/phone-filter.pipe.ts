import {Pipe, PipeTransform} from '@angular/core';
import {Phone} from "../../../../model/phone";

@Pipe({
  name: 'phoneFilter'
})
export class PhoneFilterPipe implements PipeTransform {

  transform(value: Phone[], searchTerm: string): Phone[] {
    if (!value)
      return [];

    if (!searchTerm)
      return value;

    return value.filter(phoneItem => {
      const term = searchTerm.toLowerCase();
      const valueToString = phoneItem.countryCode + " " + phoneItem.phoneNumber;
      return valueToString.toLowerCase().includes(term);
    });
  }
}
