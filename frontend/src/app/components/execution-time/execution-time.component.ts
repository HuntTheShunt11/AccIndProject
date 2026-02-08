import { Component, Input } from '@angular/core';
import {DecimalPipe} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-execution-time',
  templateUrl: './execution-time.component.html',
  imports: [
    DecimalPipe,
    TranslatePipe
  ],
  styleUrls: ['./execution-time.component.css']
})
export class ExecutionTimeComponent {
  @Input() time: number | null = null;
}
