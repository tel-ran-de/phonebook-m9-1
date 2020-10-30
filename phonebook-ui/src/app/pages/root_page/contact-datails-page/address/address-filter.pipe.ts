import {Pipe, PipeTransform} from '@angular/core';
import {Address} from "../../../../model/address";

@Pipe({
  name: 'addressFilter'
})
export class AddressFilterPipe implements PipeTransform {

  transform(value: Address[], term: string): Address[] {
    if (!value)
      return [];

    if (!term)
      return value;


    return value.filter(addressItem => {
      const valueToString = addressItem.country + " " + addressItem.city + " " + addressItem.zip + " " + addressItem.street;
      return valueToString.toLowerCase().includes(term.toLowerCase());
    });
  }
}
