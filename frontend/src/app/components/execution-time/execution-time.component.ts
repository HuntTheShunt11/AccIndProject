import { Component, Input } from '@angular/core';
import {DecimalPipe} from '@angular/common';

@Component({
  selector: 'app-execution-time',
  templateUrl: './execution-time.component.html',
  imports: [
    DecimalPipe
  ],
  styleUrls: ['./execution-time.component.css']
})
export class ExecutionTimeComponent {
  @Input() time: number | null = null;
}
