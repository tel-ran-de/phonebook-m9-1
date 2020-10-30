import {Injectable, TemplateRef} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts: any[] = [];
  toastsIds: string[] = [];

  /**
   * show toast message
   *If the ID of the displayed message is equal to the ID of the next one, then the current message will be overwritten.
   *In order that the same message IDs on the page are not repeated.
   *
   * @param textOrTpl message to display or temlate to display
   * @param options attributes like 'delay','id','autohide' etc.
   *
   */
  show(textOrTpl: string | TemplateRef<any>, options: any = {}) {

    if (options.id === null || options.id === '') {
      this.toasts.push({textOrTpl, ...options});
      return;
    }

    if (this.toastsIds.some(value => value === options.id)) {
      this.removeToastById(options.id)
      this.toasts.push({textOrTpl, ...options});
    } else {
      this.toastsIds.push(options.id);
      this.toasts.push({textOrTpl, ...options});
    }
  }

  remove(toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
    this.toastsIds = this.toastsIds.filter(t => t !== toast.id);
  }

  /**
   *
   * @param toastId Can not be null or undefined
   */
  removeToastById(toastId: string) {
    if (toastId !== null)
      this.toasts = this.toasts.filter(t => t.id !== toastId);
  }

  clearToasts() {
    this.toasts = [];
  }
}
